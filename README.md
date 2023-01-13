# DI-Library(project still in progress)
Provides API's that can be used together to help create IOC containers. Components can be used together or individually based on the users usecase. Thes API's rely heavily on Java's Reflection API.

## Current Classes
- PackageScanner -scans application packages and returns a list either all classess or filters them out by user chosen annoations.
- ContextConfiguration -constructs applications objects using their default constructor and as well as instantiating objects along with their dependencies through the use of constructor injection.
