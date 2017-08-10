#include <lame.h>
#include <jni.h>
#include <string.h>
#include <android/log.h>


#define TAG "TTJNI"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)

lame_t lame_c;
int chan;
int flag = 0;

JNIEXPORT void JNICALL
Java_com_torment_lame_LameUtils_initLame(JNIEnv *env, jobject instance, jint inSamplerate,
jint outSamplerate, jint channels) {
lame_c = lame_init();
LOGE("jni start com1");
lame_set_in_samplerate(lame_c, inSamplerate);
lame_set_out_samplerate(lame_c, outSamplerate);
lame_set_brate(lame_c, 16);   //16kBps
lame_set_num_channels(lame_c, channels);
lame_set_quality(lame_c, 7);
lame_set_mode(lame_c, MONO);

lame_set_VBR(lame_c, vbr_default);
lame_init_params(lame_c);

chan = channels;
}

JNIEXPORT void JNICALL
Java_com_torment_lame_LameUtils_wavTomp3(JNIEnv *env, jobject instance, jstring wavPath_,
jstring mp3Path_) {
if (lame_c == NULL) {
return;
}

const char *wav_path = (*env)->GetStringUTFChars(env, wavPath_, 0);
const char *mp3_path = (*env)->GetStringUTFChars(env, mp3Path_, 0);

FILE *wav_file = fopen(wav_path, "rb");
FILE *mp3_file = fopen(mp3_path, "wb");

fseek(wav_file, 2 * 1024, SEEK_CUR);

int buffer = 64 * 1024;
int read_buffer;
int write_buffer;
short int wav_buffer[buffer * chan];
unsigned char mp3_buffer[buffer];

do {
read_buffer = fread(wav_buffer, sizeof(short int) * chan, (size_t) buffer, wav_file);
if (read_buffer != 0) {
write_buffer = lame_encode_buffer(lame_c, wav_buffer, NULL, read_buffer, mp3_buffer, buffer);
fwrite(mp3_buffer, sizeof(unsigned char), (size_t) write_buffer, mp3_file);
}
if (read_buffer == 0) {
lame_encode_flush(lame_c, mp3_buffer, buffer);
}
} while (read_buffer != 0);

fclose(wav_file);
fclose(mp3_file);
}

JNIEXPORT void JNICALL
Java_com_torment_lame_LameUtils_closeLame(JNIEnv *env, jobject instance) {
if (lame_c != NULL) {
lame_close(lame_c);
}
}
