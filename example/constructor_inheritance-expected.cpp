#include <iostream>

class A {
private:
    int num;
    A * self;
public:
    A(int a, A * b) {
        num = a;
        self = b;
    }
};

class _B : public A {
public:
    _B(int a, A * b) : A(a, b) {}
};

class B : public _B {

public:
    B(int a, A * b) : _B(a, b) {
        std::cout << a << std::endl;
    }
};
