# DataParser

DataParser is a Java Archive program (.jar) that helps you generate new trial data by parsing a given statistics (Probe/Trials) and a given trialList (containing animals and their trial days).

## Getting Started

### Prerequisites

You'll need to have java `jre` and `jdk` (version 8+) installed and [maven](https://maven.apache.org/) on your `IDE` of choice.

### Installing

A step by step series of examples that tell you how to get a development env running

Install packages and build the project:
```
mvn install
```

To build with maven run:

```
mvn package
```
Excecute with:

```
mvn exec -jar {your_jar} 
```
or in nbactions file:

```xml
<action>
    <actionName>run</actionName>
        <goals>
            <!--<goal>clean</goal>-->
            <goal>-Dmaven.test.skip=true package</goal>
            <goal>org.codehaus.mojo:exec-maven-plugin:12.1:exec</goal>
        </goals>
        <properties>
            <runfx.args>-jar "${project.build.directory/${project.build.finalName}.jar"<runfx.args>
        </properties>
</action>
```
Debug:

```xml
<action>
    <actionName>debug</actionName>
    <goals>
        <goal>clean</goal>
        <goal>package</goal>
        <goal>org.codehaus.mojo:exec-maven-plugin:1.21:exec<goal>
    </goals>
    <properties>
        <runfx.args>-Xdebug-Xrunjdwp:transport=dt_socketserver=n,address={jpda.address}-Dglass.disableGrab=true -jar {project.build.directory}/{project.build.finalName.jar"</runfx.args>
        <jpda.listen>true</jpda.listen>
    </properties>
</action>        
```

The application should start with it's start screen.

## Deployment

Deployment works now with the jar file, it should have it's own folder with the jar and Templates folder in it, maybe a folder where the generated files can be put.

## Code

The java code is in two parts, the front-end stuff with javafx (all files except `LoadAndParse.java`) and logic side from LoadAndParse java class. The class takes two arguments when created: statisticsFile (`statistics.xlsx`) and trialsFile (`trials.xlsx`) files. Then you can get the headers arraylist from it by calling `getHeaders()`. The method reads the headers and returns those. Then calling `readData()` returns a hashmap of data by using list of `HeaderInfos`. You can use this list to generate a new file with wished data with `addData()` method.

```java
LoadAndParse lap = new LoadAndParse(statFile, trialsFile);
ArrayList<String> headers = lap.getAllHeaders();

HashMap<Integer, HashMap<String, Double>> hm = lap.readData(headers);
lap.addData(headers, hm);
```

## Built With

* [Javafx](https://openjfx.io/openjfx-docs/) - The client platform tool used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Poi](https://poi.apache.org/) - Used to read, write and edit `.xlxs` files

## Versioning

[tags on this repository](https://github.com/ilamal/DataParser-UI/tags). 

## Authors

* **Ilari Malinen** -  [Ilamal](https://github.com/Ilamal)
* **Mikko Nygård** -  [Mikkonyg](https://github.com/mikkonyg)
* **Tony Heikkilä** -  [Ladiladi](https://github.com/ladiladi)
* **Toni Takkinen** -  [Tonitak](https://github.com/tonitak)

Made as a group project at [University of Eastern Finland](https://uef.fi) Computer Science field.

## License

TBA

