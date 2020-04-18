# Nightlie

_Zombie killing game for Android and Desktop. The player must survive as many nights as possible. Good luck :)_

## Motivation

I started developing this game during my freshman year of college, although I have not finished it yet.
At that time, I barely knew how to develop big applications (I even didn't know how to use git). 

However, I really enjoyed watching
[Makigas Youtube Channel](https://www.youtube.com/user/MakiGAS93) as I admired his knowledge about computer related topics. I started watching every game development series he posted. Makiga's videos are very instructive and understanding; so it prompted me and encouraged me to learn more in depth about programming.

Throughout the development 
of this game I acquired several knowledge like software architecture, design patterns, and tools which helped me improve the quality 
of my code and game itself.

## What I learned

As this was my first _"serious"_ project, I learned a lot - I literally learned how to code developing it -. Especially about Java and Object Oriented Principles.
This application was my first touch with abstraction, encapsulation, inheritance and polymorphism. Although I have not exploited these principles as much as I wish, due to a lack of time inverted in this project.

When developing the AI of the enemies of the game, I faced a cost problem with the algorithm I implemented: A* pathfinding. This was due to the amount of enemies I had in-game demanding each one for a path to the player. After doing some research, I finally came up with a FlowField Algorithm. This algorithm does not depent on the amout of the enemies, unlike the A*, the cost of this algorithm depends on the size of the map itself. Even though the change of the algorithm improved a lot the efficiency of the game, it could be better. So I divided the map in 3x3 sections, having in total 9 sections. This way the game just updated the section the player was in. For a better understanding see these pair of videos I posted on Youtube: [FlowField Video](https://www.youtube.com/watch?v=jMyOyeYrkqw) and [Sections Video](https://www.youtube.com/watch?v=fNQu7NfIC4U).

In a future, I would like to further develop this game.

## Built with 🛠️

Below I mention several tools I used to develop this game:

* [Libgdx](https://libgdx.badlogicgames.com/) - Java game development framework that provides a unified API that works across Android, iOS, Windows, Linux, Mac OSX, Blackberry, and web. 
* [Gradle](https://gradle.org/) - Open-source build automation tool focused on flexibility and performance
* [Tiled](https://www.mapeditor.org/) - A flexible level editor
