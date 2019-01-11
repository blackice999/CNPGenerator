## A CNP (Cod Numeric Personal) generator written in Java

## Usage

Create an instance of CNPGenerator passing the number of entries, and then call generate() to get a list 
```java
CNPGenerator cnpGenerator = new CNPGenerator(); 
List<CNP> cnps = cnpGenerator.generate(10);
```

You can also use some predefined methods to generate CNP's by year, county for instance
```java
CNPGenerator cnpGenerator = new CNPGenerator(); 
List<CNP> cnps = cnpGenerator.generateByCounty(20, Counties.BUCHAREST);
```

If none of these methods are sufficient, create a CNPBuilder and pass in all components needed. This will generate a single CNP, so the list needs to be created by the developer
```java
CNP cnp = new CNPBuilder().setYear(1950).setDay(20).setRegistryNumber(500).createCNP();
```

