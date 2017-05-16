/*
 * com_thinkgem_jeesite_modules_judge_JudgeC.c
 *
 *  Created on: 2017年5月1日
 *      Author: mozre
 */

#include "com_thinkgem_jeesite_modules_judge_JudgeC.h"

//java字符串转C字符串
char* jstringTostr(JNIEnv* env, jstring jstr) {
	char* pStr = NULL;
	jclass jstrObj = (*env)->FindClass(env, "java/lang/String");
	jstring encode = (*env)->NewStringUTF(env, "utf-8");
	jmethodID methodId = (*env)->GetMethodID(env, jstrObj, "getBytes",
			"(Ljava/lang/String;)[B");
	jbyteArray byteArray = (jbyteArray)(*env)->CallObjectMethod(env, jstr,
			methodId, encode);
	jsize strLen = (*env)->GetArrayLength(env, byteArray);
	jbyte *jBuf = (*env)->GetByteArrayElements(env, byteArray, JNI_FALSE);
	if (jBuf > 0) {
		pStr = (char*) malloc(strLen + 1);
		if (!pStr) {
			return NULL;
		}
		memcpy(pStr, jBuf, strLen);
		pStr[strLen] = 0;
	}
	(*env)->ReleaseByteArrayElements(env, byteArray, jBuf, 0);
	return pStr;
}
//C字符串转java字符串
jstring strToJstring(JNIEnv* env, const char* pStr) {
	int strLen = strlen(pStr);
	jclass jstrObj = (*env)->FindClass(env, "java/lang/String");
	jmethodID methodId = (*env)->GetMethodID(env, jstrObj, "<init>",
			"([BLjava/lang/String;)V");
	jbyteArray byteArray = (*env)->NewByteArray(env, strLen);
	jstring encode = (*env)->NewStringUTF(env, "utf-8");
	(*env)->SetByteArrayRegion(env, byteArray, 0, strLen, (jbyte*) pStr);

	return (jstring)(*env)->NewObject(env, jstrObj, methodId, byteArray, encode);
}

JNIEXPORT jint JNICALL Java_com_thinkgem_jeesite_modules_judge_JudgeC_startJudge(
		JNIEnv * env, jobject obj, jstring jpath, jobjectArray args,
		jstring jresult) {

	char* argv[40];
	char* path;
	int i = 0;
	char* result;
	// 获取JudgeC类的引用  com/thinkgem/jeesite/modules/judge/JudgeC
	jclass clazz = (*env)->FindClass(env,
			"com/thinkgem/jeesite/modules/judge/JudgeC");
	/*	//获取构造函数的ID
	 jmethodID midInit = (*env)->GetMethodID(env, clazz, "<init>",
	 "(Ljava/lang/String;)V");*/
	// 获取数组长度
//	       jsize length = (*env)->GetArrayLength(env, arr);
	jint length = (*env)->GetArrayLength(env, args);
	//遍历args数组

	for (i = 0; i < length; i++) {
		jstring arg = (*env)->GetObjectArrayElement(env, args, i);

		argv[i] = jstringTostr(env, arg);

	}

	argv[i] = 0;

	path = jstringTostr(env, jpath);
	result = jstringTostr(env,jresult);



	return client(path, argv, result);


}
