class A {
public:
    int generate() {
        return 0;
    }
};

class _B : public A {
    int b;

private:
    bool isA;
    void go() {}

protected:
    int num(int a, int b) {
        return a+b;
    }

public:
    void hello() {}
};

class B : public _B {
private:
    bool isA;
    void go() {}

public:
    virtual void f() {}
};

int main() {
    A* a = new A;
    B* b = static_cast<_B*>(a);
//    b->f();
}
