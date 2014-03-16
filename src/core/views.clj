(ns core.views
  (:use lamina.core))

(defn chat-init [ch]
  "Initialize a new chat channel"
  (receive-all ch #(println "message: " %)))

; TODO in-rooms atom contains a map of {:room count}, checked 
; down there before hooking up a new siphon. that would be it EXCEPT
; need to be able to decrement the count when a channel closes. check out
; aleph more, then
(def in-rooms (atom { }))
(defn check-room [room] (or (@in-rooms room) 0))
(defn mod-room! [action room]
    (let [current-val (check-room room)]
        (swap! in-rooms #(assoc % room (action current-val)))))
(def inc-room! (partial mod-room! inc))
(def dec-room! (partial mod-room! dec))

(defn chat-handler [ch room]
  "Relays messages into a chat room. If it doesn't
  exist create a new channel"
  (let [chat (named-channel room chat-init)]
    (-> room inc-room! println)
    (on-closed ch #(-> room dec-room! println))
    (when (<= (check-room room) 2)
        (siphon ch chat))
    (siphon chat ch)))
