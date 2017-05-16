/*
 * judge.h
 *
 *  Created on: 2017年5月1日
 *      Author: mozre
 */

#ifndef JUDGE_H_
#define JUDGE_H_
#include <dirent.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <signal.h>
#include <stdio.h>

#define SIZE 1024




int client(char* path, char* argv[], char* result_end);


#endif /* JUDGE_H_ */
