(defn main [& args]
  (let [i [3 2 1]
        k i
        _ (println i)
        _ (println k)
        j (into [] (sort i))]
    (println i)
    (println j)
    (println k)))



        
    
        