(ns micro_adapton.adapton)

(defprotocol IAdapton
  (adapton-thunk [this])
  (adapton-result [this])
  (adapton-result-set! [this result])
  (adapton-sub [this])
  (adapton-sub-reset! [this sub])
  (adapton-sub-conj! [this sub])
  (adapton-sub-disj! [this sub])
  (adapton-super [this])
  (adapton-super-reset! [this super])
  (adapton-super-conj! [this super])
  (adapton-super-disj! [this super])
  (adapton-clean? [this])
  (adapton-clean?-set! [this b])
  (adapton-add-dcg-edge! [this a-sub])
  (adapton-del-dcg-edge! [this a-sub])
  (adapton-compute [this])
  (adapton-dirty! [this])
  (adapton-ref-set! [this val])
  (adapton->map [this]))

(deftype Adapton [^:volatile-mutable thunk 
                  ^:volatile-mutable result 
                  ^:volatile-mutable sub 
                  ^:volatile-mutable super 
                  ^:volatile-mutable clean?]
  IAdapton
  (adapton-thunk [this] (. this thunk))
  (adapton-result [this] (. this result))
  (adapton-result-set! [this res] (set! result res))
  (adapton-sub [this] (. this sub))
  (adapton-sub-reset! [this a-sub] (set! sub a-sub))
  (adapton-sub-conj! [this a-sub] (set! sub (conj (adapton-sub this) a-sub)))
  (adapton-sub-disj! [this a-sub] (set! sub (disj (adapton-sub this) a-sub)))
  (adapton-super [this] (. this super))
  (adapton-super-reset! [this a-super] (set! super a-super))
  (adapton-super-conj! [this a-super] (set! super (conj (adapton-super this) a-super)))
  (adapton-super-disj! [this a-super] (set! super (disj (adapton-super this) a-super)))
  (adapton-clean? [this] (. this clean?))
  (adapton-clean?-set! [this b] (set! clean? b))
  (adapton-add-dcg-edge! [this a-sub] (do
                                        (adapton-sub-conj! this a-sub)
                                        (adapton-super-conj! a-sub this)))
  (adapton-del-dcg-edge! [this a-sub] (do
                                        (adapton-sub-disj! this a-sub)
                                        (adapton-super-disj! a-sub this)))
  (adapton-compute [this] (if clean?
                            result
                            (do
                              (adapton-clean?-set! this true)
                              (adapton-result-set! this ((adapton-thunk this)))
                              (adapton-compute this))))
  (adapton-dirty! [this] (let [super-mod (set (map #(adapton-clean?-set! % false) super))]
                           (do
                             (adapton-clean?-set! this false)
                             (adapton-super-reset! this super-mod))))
  (adapton-ref-set! [this val] (do
                                 (adapton-result-set! this val)
                                 (adapton-dirty! this)
                                 (adapton-clean?-set! this true)))
  (adapton->map [this] {:thunk (adapton-thunk this) 
                        :result (adapton-result this)
                        :sub (adapton-sub this)
                        :super (adapton-super this)
                        :clean? (adapton-clean? this)}))

(defn mk-athunk [thunk]
  (->Adapton thunk nil #{} #{} false))

(defn adapton-ref [val]
  (->Adapton nil val #{} #{} true))
