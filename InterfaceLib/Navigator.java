package InterfaceLib;


// used for navigating between different pages in abstraction level //
// use this interface when switching pages from controllers //
// never directly from client code //

public interface Navigator {
     void goTo(String pageName);
}
