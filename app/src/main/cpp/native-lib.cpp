#include <jni.h>
#include <string>
#include  "Student.cpp"
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_zhangming14_myapplication_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    Student *student = new Student();
    student->age = 12;
    student->name = "zhangming";
    student->speak();
    return env->NewStringUTF(student->name.c_str());
}
