(ns malli-model.rep)

(defn abstract [me expr]
  (let []))

(defn parse [s o]
  (or (get-in o [:R s])
      (case (if (vector? s) (first s))
        := s
        :seqable (let [[_ c] s] [:seqable (parse c o)])
        :scope (let [[_ c] s] [:scope (parse c o)])
        :let (let [[_ R c] s
                   names (-> R keys sort vec)
                   o (update o :R merge (into {} (map (fn [k]
                                                        [k [:free k]]))
                                              (keys R)))
                   bindings (update-vals R #(parse % o))]
               (with-meta
                 [:let bindings (parse c o)]
                 {::names names})))))

(comment
  (parse [:let {:foo :bar :bar :foo} :foo] {})
  )
