#include <jni.h>
#include <string>
#include <unistd.h>
#include <sys/mman.h>
#include "log.h"


bool is_address_accessible(void* addr) {
    unsigned char vec;
    long page_size = sysconf(_SC_PAGESIZE);
    void* page_start = (void*)((long)addr & ~(page_size - 1));

    return mincore(page_start, page_size, &vec) == 0;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_test_uprobechecker_MainActivity_is_1find_1upobes(JNIEnv *env, jobject thiz) {
    bool uprobe_flag = false;
    const int bufsize = 1024;
    char filename[bufsize];
    char line[bufsize];
    FILE* fd = fopen("/proc/self/maps", "r");
    if (fd != nullptr) {
        while (fgets(line, bufsize, fd)) {
            if (strstr(line, "[uprobes]")) {
                uprobe_flag = true;
                break;
            }
        }
        fclose(fd);
    }

    if(uprobe_flag){
        return true;
    }

    uprobe_flag = is_address_accessible((void *)0x7ffffff000);
    return uprobe_flag;
}