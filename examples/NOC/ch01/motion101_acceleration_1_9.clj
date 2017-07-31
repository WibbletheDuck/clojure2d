(ns examples.NOC.ch01.motion101-acceleration-1-9
  (:require [clojure2d.core :refer :all]
            [clojure2d.math :as m]
            [clojure2d.math.random :as r]
            [clojure2d.math.vector :as v])
  (:import clojure2d.math.vector.Vec2))

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(def ^:const ^double topspeed 6.0)

(defprotocol MoverProto
  (update-mover [t w h])
  (display-mover [t canvas]))

(deftype Mover [^Vec2 position
                velocity]
  MoverProto
  (update-mover [_ w h]
    (let [acceleration (v/mult (v/generate-vec2 r/drand) (r/drand -2 2))
          nvelocity (-> velocity
                        (v/add acceleration)
                        (v/limit topspeed))
          ^Vec2 nposition (v/add position velocity)]
      (Mover. (Vec2. (m/wrap 0 w (.x nposition))
                     (m/wrap 0 h (.y nposition)))
              nvelocity)))
  (display-mover [m canvas]
    (-> canvas
        (set-background 255 255 255)
        (set-stroke 2.0)
        (set-color 127 127 127)
        (ellipse (.x position) (.y position) 48 48)
        (set-color 0 0 0)
        (ellipse (.x position) (.y position) 48 48 true))
    m))

;; run few times
(show-window (make-canvas 640 360) "Motion 101 Acceleration (1.9)"
             (fn [canvas window _ mover]
               (let [m (or mover (Mover. (Vec2. (* 0.5 ^int (width window)) (* 0.5 ^int (height window)))
                                         (Vec2. 0 0)))]
                 (display-mover (update-mover m (width window) (height window)) canvas))))