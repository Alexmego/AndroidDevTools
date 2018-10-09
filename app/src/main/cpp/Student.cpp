//
// Created by Alex on 2018/10/8.
//

//#include "Student.h"
#include <iostream>
#include "Student.h"
#include "log-utils.h"

void Student::speak() {
    cout << "my name is Alex ,i can speak english" << endl;
    LOGE("paramete is %s", "what");
}
