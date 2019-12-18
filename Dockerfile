FROM bigtruedata/scala:2.12.4

COPY target/scala-2.12/parser_combinator_example-assembly-0.1.0-SNAPSHOT.jar /home/parser_combinator_example-assembly-0.1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-cp", "/home/parser_combinator_example-assembly-0.1.0-SNAPSHOT.jar", "org.parser.combinator.example.StartServer"]
EXPOSE 8080