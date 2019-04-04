class A {
public:
    virtual ~A() {}
};

class B : public A {
public:
    B() {}
    virtual void f() {}
    ~B() override = default;
};

int main() {
    A* a = new A;
    B* b = static_cast<B*>(a);
//    b->f();
}
