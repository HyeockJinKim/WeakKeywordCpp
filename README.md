# Weak V-Table Converter

  
In C ++ code, when a superclass is *static_casted* into a subclass with a changed virtual table, an unknown function pointer is called instead of its own function. In this case, the function pointer could be vulnerable.

```c++
#include <iostream>
using namespace std;

class A {
    virtual void f() {
        cout << "A" << endl;
    }
};

class B : public A {
public:
    void non_virtual() {
        cout << "Static" << endl;
    }
    virtual void g() {
        cout << "B" << endl;
    }
};

int main() {
    A* a = new A;
    B* b = static_cast<B*>(a);
    b->f(); // No problem.
    b->g(); // Crash!!
}
```
In this example, `class B` inherits `class A`. And `class B` has a virtual function member that does not exist in `class A`. In this case,  `static_cast<B*>(a)` is possible and `b->f()` that is static function of `class A` is callable without any problems. However, `b->g()` that is virtual function of `class B` is also callable but it has type confusion.

Then, we must prevent the `b->g()` from being called when the object is *static_casted* into `class B`. We propose the following solution.

```c++
#include <iostream>
using namespace std;

class A {
    virtual void f() {
        cout << "A" << endl;
    }
};

class _B : public A { // Make Temp class that has all static function of class B
public:
    void non_virtual() {
        cout << "Static" << endl;
    }
};

class B : public _B { // B inherits temp class _B
public:
    virtual void g() {
        cout << "B" << endl;
    }
};

int main() {
    A* a = new A;
    _B* b = static_cast<_B*>(a);
    b->f(); // No problem.
    b->g(); // _B doesn't have g() virtual function 
}
```

The solution is to pre-process cpp file to prevent type confusion. Using the above solution, type confusion does not occur even if dangerous static_cast is used in the class. `subclass` with virtual function creates a `temp class` with all members except virtual function and the `temp class` inherits superclasses of the `subclass`.  
Then, `temp class` has all member of subclass except virtual function. Subclass inherits `temp class` and has only virtual function in class body. After that, change the target of `static_cast` to temp class.  

Now, the virtual function is unable to be called even if class is *static_casted* into subclass.

