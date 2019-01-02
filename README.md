## A CNP (Cod Numeric Personal) generator written in Java

## Usage

Create an instance of CNPGenerator passing the number of entries, and then call generate() to get a list 
```java
CNPGenerator cnpGenerator = new CNPGenerator(10); 
List<CNP> cnps = cnpGenerator.generate();
```