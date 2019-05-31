# Treasure-Hunt

Finds a way to treasure which is buried in map(maze)   

### Prerequisites and Running

What things you need to install the software and how to install them

```
1. Operating System : Windows 10
2. Java 1.8 or higher
3. map.txt ('@' = treasure, '-' = path, '#' = Wall) : refer map.txt for map
4. Command : java -jar clojure-1.8.0.jar treasure.clj
```

### Technical Details

```
Clojure 1.8 and Java 1.8
Clojure internally uses Java to run on JVM.
```

###Test Cases

It will print the way to treasure if exist by a series of '+' sign and '!' will be those point which are checked before reaching to treasure and are dead end.
