GrannyAndFriendsJPA
================

Granny's Addressbook + Friends with Java + Spring MVC + Neo4J





Checkout Project from git
-------------------------
1. Install git  <code>sudo apt-get install git</code>
2. Do a <code> git clone https://github.com/osintegrators/JavaNeo4Granny.git </code>

Build
-----
1. edit /src/main/resources/META-INF/spring/app-context.xml and set the "dbPath" parameter of the graphDbService bean to a path which you have read/write permission for.
2. Make sure you have maven installed, for Ubuntu users thats <code>apt-get install maven</code>, and wait.
3. In the new directory containing pom.xml, <code>mvn clean install</code>

Run
---
* This project can be deployed on any of the following
<br> Tomcat -- which is how I'll deploy it here.
<br> vFabric tcServer
<br> JBoss Application Server
5. copy the WAR files from <code>dir/to/GrannyAndFriendsJPA/target/javaneo4granny.war</code> to the tomcat webapps directory.
9. If tomcat isn't running already, start it up.
11. Browse to <code>http://localhost:8080/javaneo4granny/</code>

License
--------

Copyright (c) 2012 Open Software Integrators

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
