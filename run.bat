echo Maven
mvn clean install

echo Run the tests
java -jar target\benchmarks.jar
