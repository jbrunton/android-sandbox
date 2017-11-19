gcloud config set project "$GCLOUD_PROJECT"
gcloud auth activate-service-account --key-file ${BUDDYBUILD_SECURE_FILES}/gcloudkey.json "$GCLOUD_USER"

gcloud firebase test android run \
      --type instrumentation \
      --app $BUDDYBUILD_WORKSPACE/app/build/outputs/apk/debug/app-debug.apk \
      --test $BUDDYBUILD_WORKSPACE/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk \
      --device model=Nexus6,version=25,locale=en,orientation=portrait
