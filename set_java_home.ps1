$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot"
[Environment]::SetEnvironmentVariable("JAVA_HOME", $javaHome, "User")
Write-Host "JAVA_HOME set to $javaHome"
