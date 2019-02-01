class A {
public:
    virtual void func() {
    }
};

int main() {
    A* a = new A;
    a->func();
}