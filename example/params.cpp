class A {
};

class B : public A {
    virtual void f();
    void f(int a, int b);
};

void B::f() {
}

void B::f(int a, int b) {
    int c = a+b;
}

int main() {
    A* a = new A;
    B* b = static_cast<B*>(a);
    b->f(1, 2);
}
