class A {
public:
    int generate() {
        return 0;
    }
};

class B : public A {
    int b;
private:
    int a;
    bool isA;
    void go() {}
protected:
    int num(int a, int b) {}
public:
    void hello() {}
    virtual void f() {}
};

int main() {
    A* a = new A;
    B* b = static_cast<B*>(a);
    b->f();
}
