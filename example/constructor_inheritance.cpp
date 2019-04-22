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

class B : public A {

public:
    B(int a, A * b) : A(a, b) {
        std::cout << a << std::endl;
    }
};
