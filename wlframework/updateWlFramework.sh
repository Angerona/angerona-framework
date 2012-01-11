mkdir tlib
for lib in "WlWindows" "WlConfiguration" "WlMacWidgets" "WlPreview" "WlResourceManager" "WlUtilities"; do
echo $lib
cd $lib/bin/
jar cf ../../tlib/$lib.jar ./*
cd ../../
mvn install:install-file -Dfile=tlib/$lib.jar -DgroupId=com.whiplash -DartifactId=$lib -Dversion=0.1.22 -Dpackaging=jar -DupdateReleaseInfo=true
done
