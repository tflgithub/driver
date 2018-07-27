# Local build
1) Build using the same command as in the latest pipeline in gitlab.
2) (Optional) Move build to some location: `mv app/build/outputs/apk/*.apk driver.apk`
3) Connect device and upload using `~/Library/Android/sdk/platform-tools/adb install -r driver.apk`

See crash logs:
```
cat storage/sdcard0/Android/data/fodel.com.fodelscanner/cache
```