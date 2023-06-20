#!/bin/bash

SERVER_NAME="$1"
LOG_FILE="$2"
TMP_FILE="/path/to/temp_file.txt"

# Regular expressions to search for error, exception, or warning patterns
ERROR_PATTERN="error"
EXCEPTION_PATTERN="exception"
WARNING_PATTERN="warning"

tail -f "$LOG_FILE" | while read -r line
do
    # Check for error, exception, or warning patterns and extract relevant information
    if [[ $line =~ $ERROR_PATTERN ]]; then
        echo "[$SERVER_NAME] Error: $line" >> "$TMP_FILE"
    elif [[ $line =~ $EXCEPTION_PATTERN ]]; then
        echo "[$SERVER_NAME] Exception: $line" >> "$TMP_FILE"
    elif [[ $line =~ $WARNING_PATTERN ]]; then
        echo "[$SERVER_NAME] Warning: $line" >> "$TMP_FILE"
    fi
done
