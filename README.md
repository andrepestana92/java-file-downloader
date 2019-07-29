# java-file-downloader

## Input pattern
The program expects an input <FILE_URL_LIST> to a file with a list of url sources having one url per line, and an optional parameter <DOWNLOAD_FOLDER> where the program will save those files. If this second parameter is not present, the program will create a folder named `downloads` and save the files there

## How to run with Maven:
You can build a jar with all dependencies with the command:
`mvn clean install assembly:single`
And then running the comand in the terminal
`java -cp target/java-file-downloader-0.0.1-SNAPSHOT-jar-with-dependencies.jar main.java.org.dowloader.InputReceiver <FILE_URL_LIST> <DOWNLOAD_FOLDER>`

If you don't want to create a jar file, you can execute the command
`mvn exec:java -Dexec.args "<FILE_URL_LIST> <DOWNLOAD_FOLDER>"`

You can also configure those build steps in eclipse.

## How to run with eclipse
Within eclipse IDE, you can run a `mvn install` to install the dependencies and run the program by clicking in the Run button