# DI-Library(project still in progress)
Provides utility API's that can be used together to help implement IOC and dependency injection. Components can be used together to construct objects and their dependencies at runtime. Components can also be utilized individually if needed. This library relies heavily on Java's Reflection API.

## Current Classes
- PackageScanner -scans application packages and returns a list, either all classess, or filters them out by user specified annoations.
- ContextConfiguration -constructs applications objects using their default constructor and as well as instantiating objects along with their dependencies through the use of constructor injection.
