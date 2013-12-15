(ns ld28.core
  (:import (java.net Socket)
           (java.io PrintWriter InputStreamReader BufferedReader))
  (:gen-class))

(def server { :name "skirnismal.morgawr.com" :port 9995})

(defn connect 
  [server]
  (let [s (Socket. (:name server) (:port server))
        in (BufferedReader. (InputStreamReader. (.getInputStream s)))
        out (PrintWriter. (.getOutputStream s))
        conn {:in in :out out :socket s}]
    conn))

(defn write-msg 
  [c msg]
  (.write (:out c) msg)
  (.write (:out c) "\n")
  (.flush (:out c)))

(defn read-msg
  [c]
  (.readLine (:in c)))


(def intro ["PRESS ENTER TO CONTINUE WITH THE STORY"
            "Your name is Mikhail. Your mother was a famous Russian ropedancer, you have always admired her ever since you were a young child."
            "You have been traveling the world with your mother's circus entourage for years now. Unfortunately, your mother died five years ago in a performance accident, since then you've been trying to always help around the circus."
            "You never had any real talent but it's been three years now since you started taking knife-throwing lectures, you feel confident. You practiced and you know you are ready to hit the big scene with your crew."
            "You slowly step on stage as your assistant, a young girl who decided to volunteer and help, goes to the throwing board."
            "Flashes everywhere, you can hear the shouting of encouragement from the public, they are very excited about your performance. You nervously look around, you can feel a tiny drop of sweat going down your back."
            "You grab the knife in your hand, suddenly the crowd becomes silent. You can hear your own breath as you take aim. The whole circus seems to be breathing with you."
            "In."
            "Out."
            "In."
            "Out."
            "You know you can do it, you have done it before. You only get one chance to prove you're worthy, you only get one throw. You will do it. There is no other way."
            "You need to take these parameters in consideration before making the throw:"])

(def wind "There is a breeze blowing from the %s at approximately %d m/s.")
(def distance "The distance between you and the throwing board is %d meters.")
(def weight "The knife weights exactly %d grams.")

(def choice { 0 "top"
              1 "bottom"
              2 "left"
              3 "right"
              4 "north"
              5 "south"
              6 "ceiling"
              7 "west"
              8 "east" })

(def wrong { 0 "strength"
             1 "slope" 
             2 "inclination"
             3 "cohesive insulation"
             4 "friction" })

(def amt { 0 "much"
           1 "low"})

(def bodypart { 0 "head"
                1 "neck"
                2 "chest"
                3 "guts"
                4 "brain"
                5 "right eye"
                6 "left eye"
                7 "heart" })

(defn rand-range
  [a b]
  (+ a (rand-int b)))

(defn write-with-wait
  [s]
  (print s)
  (.flush *out*)
  (read-line))

(defn main-game []
  (doseq [s intro]
    (write-with-wait s))
  (println (format wind  (choice (rand-int 9)) (rand-range 0 10)))
  (println (format distance (rand-range 5 50)))
  (println (format weight (rand-range 100 300)))
  (println "Specify how much force you want to give to the throw (in Newtons):")
  (read-line)
  (println "Specify the angle of direction you want to give to the throw (in radians):")
  (read-line)
  (println "Specify the height of the throw (be sure to calculate the slope covariance):")
  (read-line)
  (println "Calculating...")
  (Thread/sleep 1000)
  (println "Still calculating...")
  (Thread/sleep 2000)
  (println "You throw the knife with all you have, you know it's going to hit and you've trained so much for this. You can't get it wrong.")
  (println "PRESS ENTER TO CONTINUE")
  (read-line)
  (println "Unfortunately, you realize too late that your calculations were wrong, you should've known better!")
  (println (format "The amount of %s was too %s for this situation." (wrong (rand-int 5)) (amt (rand-int 2))))
  (println "...")
  (Thread/sleep 2000)
  (println (format "You can see the knife lodged into the poor girl's %s as she tries to scream for help. The crowd gasps in surprise, some children start screaming as their parents try to cover their eyes." (bodypart (rand-int 8))))
  (println "You had one chance, and you blew it.")
  (println "You stare at the girl speechless as she slow drifts into the peaceful embrace of death.")
  (println "GAME OVER."))

(defn -main [& args]
  (let [c (connect server)]
    (write-msg c "check")
    (let [s (read-msg c)]
      (if (= "no" s)
        (do
          (println "You already had your chance, the circus will never let you perform ever again.")
          (println "That poor young girl is dead now, there is nothing you can do.")
          (println "You only got that chance, and you ruined it.")
          (Thread/sleep 2000)
          (println "#YOGO")
          (println "GAME OVER."))
        (do 
          (main-game)
          (write-msg c "mark")
          (read-line)))
      (.close (:socket c)))))
