;; Checks the homepage tag cloud against the real shape of README.md.
;;
;; docs/templates/generic-home.html sizes each category by how many entries sit
;; under it, and that tally is kept by hand. Nothing else notices when it
;; drifts: docs/check-site.sh asserts the cloud EXISTS, not that its numbers are
;; right, so a stale count renders perfectly and simply misinforms. It had gone
;; five sections stale before anyone looked.
;;
;; Run it with `bb site:counts`. CI runs it from docs/check-site.sh under jolt,
;; which is the runtime the shared workflow installs, so everything below stays
;; inside what both runtimes agree on.

(require '[clojure.string :as str])

(def readme-path "README.md")
(def template-path "docs/templates/generic-home.html")

;; Housekeeping sections carry no entries a reader would browse by, so the
;; cloud leaves them out on purpose. Keep in step with generic-home.html.
(def untagged
  #{"Contents" "Community and Support" "Contributing" "License"})

;; Tier thresholds, as documented in generic-home.html.
(defn tier [n]
  (cond (<= n 5)  "tag-1"
        (<= n 15) "tag-2"
        (<= n 35) "tag-3"
        :else     "tag-4"))

(defn anchor
  "A heading's slug, by the rules this README actually exercises."
  [heading]
  (-> heading
      str/lower-case
      (str/replace (re-pattern "[^a-z0-9 -]") "")
      (str/replace " " "-")))

(defn tally
  "Every `## ` section of the README in order, with how many `- [` entries
   sit under each one."
  [text]
  (let [heading-re (re-pattern "^## (.+)$")
        step (fn [acc line]
               (let [h (second (re-find heading-re line))]
                 (cond
                   h (-> acc (assoc :current h) (update :order conj h))

                   (and (:current acc) (str/starts-with? line "- ["))
                   (update-in acc [:counts (:current acc)] (fnil inc 0))

                   :else acc)))]
    (reduce step {:current nil :order [] :counts {}} (str/split-lines text))))

(defn cloud
  "Every link in the tag cloud, as the template declares it."
  [text]
  (let [tag-re (re-pattern
                "class=\"(tag-\\d)\" href=\"#([a-z0-9-]+)\">(.*?)<span class=\"count\">(\\d+)</span>")]
    (map (fn [[_ t a label n]]
           {:tier t :anchor a :label label :count (parse-long n)})
         (re-seq tag-re text))))

(defn mismatches
  "One line per disagreement between the README and the tag cloud."
  [expected actual]
  (let [by-anchor (reduce (fn [m e] (assoc m (:anchor e) e)) {} actual)
        known (set (map :anchor expected))]
    (concat
     (for [e expected
           :when (not (contains? by-anchor (:anchor e)))]
       (str (:label e) ": missing from the tag cloud (" (:count e)
            " entries, would be " (tier (:count e)) ")"))

     (for [e expected
           :let [a (get by-anchor (:anchor e))]
           :when a
           :let [notes (remove nil?
                               [(when (not= (:count e) (:count a))
                                  (str "count is " (:count a) ", README has " (:count e)))
                                (when (not= (:tier e) (:tier a))
                                  (str "tier is " (:tier a) ", " (:count e) " entries want " (:tier e)))
                                (when (not= (:label e) (:label a))
                                  (str "label is " (pr-str (:label a)) ", heading is " (pr-str (:label e))))])]
           :when (seq notes)]
       (str (:label e) ": " (str/join "; " notes)))

     (for [a actual
           :when (not (contains? known (:anchor a)))]
       (str (:label a) ": points at #" (:anchor a) ", which is not a README section")))))

(let [{:keys [order counts]} (tally (slurp readme-path))
      expected (for [h order
                     :when (not (contains? untagged h))
                     :let [n (get counts h 0)]]
                 {:tier (tier n) :anchor (anchor h) :label h :count n})
      problems (mismatches expected (cloud (slurp template-path)))]
  (if (seq problems)
    (do
      (println "the tag cloud is out of step with README.md:")
      (doseq [p problems] (println "  -" p))
      (println)
      (println "re-tally" template-path "against the README, then run this again")
      (System/exit 1))
    (println (str "tag cloud matches the README: " (count expected)
                  " categories, " (reduce + (map :count expected)) " entries"))))
