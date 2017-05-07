# Hazelcast Serialization

This is a Spring-Boot project consisting of almost all of the examples provided in this [post](http://blog.acari.io/java/2017/05/01/Hazelcast-Performance-Serialization.html).
Which covers optimizations that can be made to make your Hazelcast experience amazing!
It has time trial tests for both a Hazelcast server and a Client connected to a vanilla Hazelcast server.

To run the sample you will need:
 - Internet Connection (At least the first time it is run)
 - [Java 8 runtime](http://blog.acari.io/jvm/2017/05/05/Gradle-Install.html)
 - [Gradle 2.3+ ](http://blog.acari.io/jvm/2017/05/05/Gradle-Install.html)
 
Once the repository is on your machine, in order to boot up the server do the following.

1. Open up a command window and make the current working directory the root of the hazelcast-serialization repository
1. Run the command

        ./gradlew bootRun
        
The application will output a bunch of logs. Somewhere in there you should find the outputs of the time trials.

    ....
    ....
    2017-05-04 13:06:46.694  INFO 10982 --- [           main] io.acari.TimeTrial                       : Time trials ready for Hazelcast server ready to start!
    2017-05-04 13:06:46.730  INFO 10982 --- [onPool-worker-1] c.h.i.p.impl.PartitionStateManager       : [172.20.0.1]:9001 [dev] [3.8.1] Initializing cluster partition table arrangement...
    2017-05-04 13:06:53.897  INFO 10982 --- [           main] io.acari.TimeTrial                       : Writing 40000 Regular Serializable  arguments 10 times took an average of 263.6 milliseconds.
    2017-05-04 13:06:53.898  INFO 10982 --- [           main] io.acari.TimeTrial                       : Reading 40000 Regular Serializable  arguments 10 times took an average of 437.7 milliseconds.
    2017-05-04 13:06:56.691  INFO 10982 --- [ration.thread-0] c.h.i.p.impl.PartitionStateManager       : [127.0.0.1]:9009 [clientExample] [3.8.1] Initializing cluster partition table arrangement...
    2017-05-04 13:06:56.699  INFO 10982 --- [           main] io.acari.TimeTrial                       : Writing 40000 Externalizable  arguments 10 times took an average of 140.1 milliseconds.
    2017-05-04 13:06:56.699  INFO 10982 --- [           main] io.acari.TimeTrial                       : Reading 40000 Externalizable  arguments 10 times took an average of 125.1 milliseconds.
    2017-05-04 13:06:58.950  INFO 10982 --- [           main] io.acari.TimeTrial                       : Writing 40000 Data Serializable  arguments 10 times took an average of 130.2 milliseconds.
    2017-05-04 13:06:58.951  INFO 10982 --- [           main] io.acari.TimeTrial                       : Reading 40000 Data Serializable  arguments 10 times took an average of 82.0 milliseconds.
    2017-05-04 13:07:01.164  INFO 10982 --- [           main] io.acari.TimeTrial                       : Writing 40000 Identified Data Serializable  arguments 10 times took an average of 127.4 milliseconds.
    2017-05-04 13:07:01.164  INFO 10982 --- [           main] io.acari.TimeTrial                       : Reading 40000 Identified Data Serializable  arguments 10 times took an average of 78.7 milliseconds.
    2017-05-04 13:07:01.164  INFO 10982 --- [           main] io.acari.TimeTrial                       : Time trials for Hazelcast server finished!
    2017-05-04 13:07:01.164  INFO 10982 --- [           main] io.acari.TimeTrial                       : Time trials ready for Hazelcast client ready to start!
    2017-05-04 13:07:07.450  INFO 10982 --- [           main] io.acari.TimeTrial                       : Writing 40000 Regular Serializable  arguments 10 times took an average of 218.7 milliseconds.
    2017-05-04 13:07:07.450  INFO 10982 --- [           main] io.acari.TimeTrial                       : Reading 40000 Regular Serializable  arguments 10 times took an average of 397.4 milliseconds.
    2017-05-04 13:07:10.063  INFO 10982 --- [           main] io.acari.TimeTrial                       : Writing 40000 Externalizable  arguments 10 times took an average of 129.5 milliseconds.
    2017-05-04 13:07:10.063  INFO 10982 --- [           main] io.acari.TimeTrial                       : Reading 40000 Externalizable  arguments 10 times took an average of 116.7 milliseconds.
    2017-05-04 13:07:12.214  INFO 10982 --- [           main] io.acari.TimeTrial                       : Writing 40000 Data Serializable  arguments 10 times took an average of 123.1 milliseconds.
    2017-05-04 13:07:12.215  INFO 10982 --- [           main] io.acari.TimeTrial                       : Reading 40000 Data Serializable  arguments 10 times took an average of 80.2 milliseconds.
    2017-05-04 13:07:14.265  INFO 10982 --- [           main] io.acari.TimeTrial                       : Writing 40000 Identified Data Serializable  arguments 10 times took an average of 121.7 milliseconds.
    2017-05-04 13:07:14.265  INFO 10982 --- [           main] io.acari.TimeTrial                       : Reading 40000 Identified Data Serializable  arguments 10 times took an average of 72.2 milliseconds.
    2017-05-04 13:07:14.265  INFO 10982 --- [           main] io.acari.TimeTrial                       : Time trials for Hazelcast client finished!
    .....
    .....
    
Again, I now know that there are frameworks for benchmarking now. 
However that was after I spent time writing the sample project.

Enjoy!

## -Alex
