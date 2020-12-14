file="$1"

if [ -f "$file" ]; then
  echo "Deleting $file"
  rm "$file"
else
  echo "$file does not exist"
fi
