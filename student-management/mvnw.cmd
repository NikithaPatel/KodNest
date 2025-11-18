@ECHO OFF
setlocal
SET WRAPPER_DIR=%~dp0\.mvn\wrapper
SET WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar
IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO Maven wrapper JAR missing: %WRAPPER_JAR%
  EXIT /B 1
)

SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
IF NOT EXIST "%JAVA_EXE%" SET JAVA_EXE=java

"%JAVA_EXE%" -Dmaven.multiModuleProjectDirectory=%CD% -cp "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
ENDLOCAL
