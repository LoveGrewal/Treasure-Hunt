;---------------------------------------------------------------------------------
; Loveshant Grewal
; 40094202
; Assignment 2
;---------------------------------------------------------------------------------

;---------------------------------------------------------------------------------
; namespace
;---------------------------------------------------------------------------------
(ns comparative.core
(:require [clojure.string :as str])
(:gen-class)
)

;---------------------------------------------------------------------------------
; references
;---------------------------------------------------------------------------------
(use 'clojure.java.io)

;---------------------------------------------------------------------------------
; variable declaration
;---------------------------------------------------------------------------------
(def treasure-map [])
(def trail [])
(def blockedTrail [])
(def lengthOfMap (atom 0))
(def heightOfMap (atom 0))

;---------------------------------------------------------------------------------
; custom functions
;---------------------------------------------------------------------------------

;---------------------------------------------------------------------------------
;This function will read a map from text file and place the map in global variable
;---------------------------------------------------------------------------------
(defn readMap "read the map and storing in list" [file]
	(with-open [rdr (reader file)]
		(doseq [line (line-seq rdr)]
			(def treasure-map (conj treasure-map line))
			(reset! lengthOfMap (count line))
			(swap! heightOfMap inc)
		)
	)	
)

;---------------------------------------------------------------------------------
;This function will give first integer in string
;---------------------------------------------------------------------------------
(defn parse-int [s]
   (Integer. (re-find  #"\d+" s )))

;---------------------------------------------------------------------------------
;This function will return string at row and column of map
;---------------------------------------------------------------------------------
(defn atLoc "find what is at loc" [r c]
	(def rowth (get treasure-map r))
	(subs rowth c (+ c 1))
)

;---------------------------------------------------------------------------------
;This function will keep trail of the path
;---------------------------------------------------------------------------------
(defn addTrail [x y]
	(def trail (conj trail (format "%d,%d" x y)))
)

;---------------------------------------------------------------------------------
;This function will pop trail of the path
;---------------------------------------------------------------------------------
(defn popTrail []
	(def trail (pop trail))
	(def post )
	(if(> (count trail) 0)
		(do (def a (get trail (- (count trail) 1))) (format a))
	(format "noElementsFound"))	
)

;---------------------------------------------------------------------------------
;This function to check if trail contains coordinates
;---------------------------------------------------------------------------------
(defn containsCoordinateByTrail [x y]
	(def a(set trail))
	(if(contains? a (format "%d,%d" x y))
		(format "true")
	(format "false"))
)

;---------------------------------------------------------------------------------
;This function will keep blocked trail of the path
;---------------------------------------------------------------------------------
(defn addBlockedTrail [x y]
	(def blockedTrail (conj blockedTrail (format "%d,%d" x y)))
)

;---------------------------------------------------------------------------------
;This function to check if blockedTrail contains coordinates
;---------------------------------------------------------------------------------
(defn containsCoordinateByBlockedTrail [x y]
	(def a(set blockedTrail))
	(if(contains? a (format "%d,%d" x y))
		(format "true")
	(format "false"))
)

;---------------------------------------------------------------------------------
;This function will return neighbour where there is path
;---------------------------------------------------------------------------------
(defn findPath [x y]
	(def xinc (+ x 1))
	(def yinc (+ y 1))
	(def xdec (- x 1))
	(def ydec (- y 1))
	(cond
		(and (< xinc @heightOfMap) (< y @lengthOfMap) (or (= (atLoc xinc y) "@") (= (atLoc xinc y) "-")) (= (containsCoordinateByTrail xinc y) "false") (= (containsCoordinateByBlockedTrail xinc y) "false")) 
			(format "%d,%d" xinc y)
		(and (< x @heightOfMap) (< yinc @lengthOfMap) (or (= (atLoc x yinc) "@") (= (atLoc x yinc) "-")) (= (containsCoordinateByTrail x yinc) "false") (= (containsCoordinateByBlockedTrail x yinc) "false")) 
			(format "%d,%d" x yinc)
		(and (>= xdec 0) (< y @lengthOfMap) (or (= (atLoc xdec y) "@") (= (atLoc xdec y) "-")) (= (containsCoordinateByTrail xdec y) "false") (= (containsCoordinateByBlockedTrail xdec y) "false"))
			(format "%d,%d" xdec y)
		(and (< x @heightOfMap) (>= ydec 0) (or (= (atLoc x ydec) "@") (= (atLoc x ydec) "-")) (= (containsCoordinateByTrail x ydec) "false") (= (containsCoordinateByBlockedTrail x ydec) "false"))
			(format "%d,%d" x ydec)
		:else (format "noChoice")		
	)
)



;---------------------------------------------------------------------------------
;This function will find path to treasure if it is there
;---------------------------------------------------------------------------------
(defn findTrail [x y]
(def currentPosRow (atom x))
(def currentPosCol (atom y))
(def previousPosRow (atom x))
(def previousPosCol (atom y))
(addTrail x y)
(if(= (atLoc x y) "-")
	(do 
		(while (and (or (not= (findPath @currentPosRow @currentPosCol) "noChoice") (> (count trail) 0)) (not= (atLoc @currentPosRow @currentPosCol) "@"))
			(do
				(cond
					(not= (findPath @currentPosRow @currentPosCol) "noChoice")
					(do 
						(reset! previousPosRow @currentPosRow)
						(reset! previousPosCol @currentPosCol)
						(def newPos (findPath @currentPosRow @currentPosCol))
						(def temp (str/split newPos #"\,"))
						(reset! currentPosRow (parse-int (get temp 0)))
						(reset! currentPosCol (parse-int (get temp 1)))
						(addTrail @currentPosRow @currentPosCol)						
					)
					(> (count trail) 0)
					(do 
						(reset! previousPosRow @currentPosRow)
						(reset! previousPosCol @currentPosCol)
						(addBlockedTrail @currentPosRow @currentPosCol)
						(def newPos (popTrail))
						(if(not= newPos "noElementsFound")
							(do(def temp (str/split newPos #"\,"))
								(reset! currentPosRow (parse-int (get temp 0)))
								(reset! currentPosCol (parse-int (get temp 1)))
							)
						)
					)
				)
						
			)
		)
	)
(println "no starting point"))

)

;---------------------------------------------------------------------------------
;This function will print solution at location x and y
;---------------------------------------------------------------------------------
(defn printAtLoc [x y]
	(cond
		(= (atLoc x y) "@") (print "@")
		(= (containsCoordinateByTrail x y) "true") (print "+")
		(= (containsCoordinateByBlockedTrail x y) "true") (print "!")
		:else (print (atLoc x y))
	)
)

;---------------------------------------------------------------------------------
;This function will print if the path is found to treasure with path to treasure
;---------------------------------------------------------------------------------
(defn printSolutionMap []
	(def ith (atom 0))
	(def jth (atom 0))
	(while (< @ith @heightOfMap)
  (do (println)(reset! jth 0)(while (< @jth @lengthOfMap) (do (printAtLoc @ith @jth) (swap! jth inc)))
  				(swap! ith inc)
  )
 )
)

;---------------------------------------------------------------------------------
;This function will print if the path is found to treasure with path to treasure
;---------------------------------------------------------------------------------
(defn printOriginalMap []
	(doseq [x treasure-map] 
  (println x)
 )
)


;---------------------------------------------------------------------------------
;This function will validate treasure map
;---------------------------------------------------------------------------------
(defn validateMap []
	(def sameLen (atom 0))
	(def tempLen (atom 0))
	(cond
		(and (= @heightOfMap 0) (= @lengthOfMap 0)) (reset! sameLen 1)
		(and (> @heightOfMap 0) (> @lengthOfMap 0)) 
			(do 
				(reset! tempLen (count (get treasure-map 0)))
				(dotimes [i @heightOfMap] (do 
																															(if (not= @tempLen (count (get treasure-map i))) (swap! sameLen inc))
																														)
				)
			)
	)
	

	(if (= @sameLen 0)
		(format "ok")
	(format "notOk"))
)

;---------------------------------------------------------------------------------
;This function contain the sequence to find path
;---------------------------------------------------------------------------------
(defn -main "comment" [& args]
	(readMap "map.txt")
	(if (= (validateMap) "ok")
		(do 
			(println)
			(println "Existing map : ")
			(println)
			(printOriginalMap)
			(cond
				(= (atLoc 0 0) "@") (addTrail 0 0)
				(= (atLoc 0 0) "#") (println "no starting point")
				:else (findTrail 0 0)
			)(println)
			(if (> (count trail) 0)
			(println "Treasure found")
			(println "No way to treasure"))
			(println)
			(printSolutionMap)
			(println)
		)
	(println "Invalid Map. Please make sure that map has equal length rows")
	)
	
)

;---------------------------------------------------------------------------------
;Invoking the main function
;---------------------------------------------------------------------------------
(-main)