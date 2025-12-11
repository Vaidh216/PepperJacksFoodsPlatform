#!/bin/bash

# Food Platform Service - Build Script
# This script builds the project using Java 21

echo "🚀 Building Food Platform Service..."
echo ""

# Set Java 21
export JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu-21.jdk/Contents/Home

# Verify Java version
echo "Using Java:"
$JAVA_HOME/bin/java -version
echo ""

# Clean and build
echo "Running: mvn clean package -DskipTests"
mvn clean package -DskipTests

# Check if build succeeded
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ BUILD SUCCESSFUL!"
    echo ""
    echo "JAR Location: target/food-platform-service-1.0.0.jar"
    echo ""
    echo "To run the application:"
    echo "  ./run.sh"
    echo "  OR"
    echo "  mvn spring-boot:run"
else
    echo ""
    echo "❌ BUILD FAILED"
    echo "Please check the errors above."
    exit 1
fi

