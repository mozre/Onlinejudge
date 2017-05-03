/*
 * Judge.c
 *
 *  Created on: 2017年4月16日
 *      Author: mozre
 */
#include "judge.h"

// not any meaning
int ERR_UNABLE_TOUCH_NUM = 1;
// fork fail
int ERR_SYSTEM_NUM = 2;
// exec cmdline fail
int ERR_EXCMDLINE_NUM = 3;
// out memory of array
int ERR_OUT_MEMORY_OF_ARRAY = 4;
// compile erro
int ERR_COMPILE = 5;
// code round trips
int ERR_EXEC_CIR = 7;
// get a mistake result
int ERR_RESULT = 8;
// success exec child process work
int SUCC_COMPLILE_NUM = 11;
// success exec
int SUCC_EXEC_NUM = 10;
// compile / exec -> rigth result
int SUCC_COMPILE_AND_EXEC_CHECKOUT = 9;

char* CLANG = "clang";
char* OUTPUT_TARGET = " -o ";
char * FILE_END = ".txt";

int resultCode = 0;
int cc = 200;

void signal_handler() {

	exit(ERR_EXEC_CIR);

}

char* getTarget(char* p) {
	static char buf[40];
	int i = 0;
	while (*p != '.') {
		buf[i++] = *p;
		++p;
	}
	buf[i] = 0;
	p = buf;
	printf("target buf = %s\n", p);
	return p;
}

char* rmspace(char* or) {

	char buf[SIZE] = "";
	char * p;
	int len = 0;
	int count = 0;
	int i = 0;
	len = strlen(or);
	for (i = 0; i < len; ++i) {

		if (or[i] == ' ' || or[i] == '\n') {

			continue;

		}
		buf[count++] = or[i];

	}
	printf("jjj buf = %s\n", buf);
	p = buf;
	return p;

}
int excute(char* target, char* argv[], char* result) {

	char buf[SIZE] = "";
	char filename[40] = "";
	char* cmds[20];
	int fd = -1;
	int count = 0;
	int len_res;
	char * end_result = "";
	int pid;
	int retry = 1;
	int save_fd = -1;
	int status = -1;
	int tmp = -1;
	int SF = -1;
	int dsd = -1;

	printf("\nkkkkk\n");
	strcat(filename, target);
	strcat(filename, FILE_END);
	strcat(buf, target);
	printf("target = %s\n", target);
	cmds[count] = buf;
	while (argv[count]) {
		if (count > 18) {
			//TODO 此处应考虑是否因为越界而返回
			return -3;
		}
		cmds[count + 1] = argv[count];
		++count;
	}
	cmds[count + 1] = 0;
	printf("-----%s------\n", cmds[0]);

	count = 0;
	while (cmds[count]) {

		printf("%s\n", cmds[count++]);

	}

	if ((pid = fork()) < 0) {

		exit(ERR_SYSTEM_NUM);
	} else if (pid == 0) {
		alarm(5);
		signal(SIGALRM, SIG_DFL);
		if ((fd = open(filename, O_WRONLY | O_TRUNC | O_CREAT), 00777) < 0) {

			exit(ERR_SYSTEM_NUM);

		}

		save_fd = dup(STDOUT_FILENO);
		dup2(fd, STDOUT_FILENO);
		close(fd);
		tmp = execvp(cmds[0], cmds);
		printf("tmp = %d\n", tmp);
		dup2(save_fd, STDOUT_FILENO);
		close(save_fd);
		if (tmp == -1) {

			exit(ERR_EXCMDLINE_NUM);
		}
		exit(SUCC_EXEC_NUM);
	}

	wait(&status);

	dsd = WIFEXITED(status);
	SF = WEXITSTATUS(status);
	printf("exec ---------> SF = %d\n", SF);
	if (SF == ERR_EXEC_CIR) {

		return ERR_EXEC_CIR;
	} else if (SF == ERR_EXCMDLINE_NUM) {
		return ERR_EXCMDLINE_NUM;
	}
	len_res = strlen(result);
	memset(buf, '\0', sizeof(buf));

	if (len_res < SIZE) {

		len_res = 0;
		fd = -1;
		if ((fd = open(filename, O_RDONLY)) < 0) {

			exit(ERR_SYSTEM_NUM);

		}
		printf("-------------");
		if ((len_res = read(fd, buf, SIZE)) > 0) {

			if (len_res >= SIZE - 1) {
				// TODO 读入内容过长

				return ERR_OUT_MEMORY_OF_ARRAY;
			} else {

				end_result = rmspace(buf);
				printf("buf = %s\nend_result = %s\n", buf, end_result);
				if (strcmp(result, end_result) == 0) {

					printf("success");
					return SUCC_COMPILE_AND_EXEC_CHECKOUT;
					//TODO sucess
				} else {

					printf("result erro\n");
					//TODO 结果不正确
					return ERR_RESULT;
				}

			}

		}

	} else {

		return ERR_OUT_MEMORY_OF_ARRAY;
		//TODO 结果过长 数组过小

	}
	return -1;
}

int client(char* path, char* argv[], char* result_end) {

	char buf[SIZE] = "";
	char filename[40] = "";
	char* arg[20];
	int result = 0;
	int pos = 0;
	int fd = -1;
	char* p;
	char* target = "";
	int SF = -1;
	int dsd = -1;
	int t_fd;

	int pid;
	int retry = 1;
	int save_in = -1;
	int save_err = -1;
	int status = -1;
	int tmp = -1;

	if (SIZE < (strlen(path) + strlen(OUTPUT_TARGET))) {
		return ERR_OUT_MEMORY_OF_ARRAY;
	}
	strcat(buf, CLANG);
	strcat(buf, " ");
	strcat(buf, path);
	strcat(buf, OUTPUT_TARGET);
	target = getTarget(path);

	strcat(buf, target);
	strcat(filename, target);
	strcat(filename, FILE_END);
	printf("filename = %s\n", filename);
	p = buf;
	arg[pos++] = p;
	while (*p) {
		if (*p == ' ') {

			*p = '\0';
			++p;
			while (*p == ' ') {
				++p;
			}
			arg[pos++] = p;
		}

		++p;

	}
	arg[pos] = 0;
	pos = 0;
	while (arg[pos]) {

		printf("%s\n", arg[pos++]);

	}

	if ((pid = fork()) < 0) {

		//	printf("errrrrrr\n");
	} else if (pid == 0) {
		alarm(5);
		signal(SIGALRM, SIG_DFL);
		if ((fd = open(filename, O_WRONLY | O_TRUNC | O_CREAT, 00777)) < 0) {
			exit(ERR_SYSTEM_NUM);
		}
		save_in = dup(STDIN_FILENO);
		save_err = dup(STDERR_FILENO);
		dup2(fd, STDIN_FILENO);
		dup2(fd, STDERR_FILENO);
		close(fd);
		tmp = execvp(CLANG, arg);
		dup2(save_in, STDIN_FILENO);
		dup2(save_err, STDERR_FILENO);
		printf("tmp = %d\n", tmp);
		cc = tmp;
		if (tmp = -1) {
			exit(ERR_COMPILE);
		}
		exit(SUCC_COMPLILE_NUM);
	}

	wait(&status);
	dsd = WIFEXITED(status);
	SF = WEXITSTATUS(status);
	if (SF == ERR_COMPILE) {
		return ERR_COMPILE;
	}

	if ((t_fd = open(target, O_RDONLY)) == -1) {
		printf("make target fail\n");
		return ERR_COMPILE;
	} else {
		close(t_fd);
	}

	printf("SF = %d cc = %d\n", SF, cc);

	printf("-------------------start excute---------------------\n");
	printf("path = %s  filename = %s   target = %s \n", path, filename, target);
	resultCode = excute(target, argv, result_end);
	return resultCode;

}

//int main() {
//
//	char* argv[] = { "1", "2", 0 };
//	int re = -200;
//	re = client("/home/mozre/MS/CMake/tt.c", argv, "12");
//	printf("main: re = %d\n", re);
//
//}
