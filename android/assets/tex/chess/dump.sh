#!/bin/bash

shopt -s extglob

rm -rf *.png

word="+pPoOnNmMbBvVrRtTqQwWkKlL"

for i in $(seq 1 ${#word})
do
	c="${word:i-1:1}"
	name="$c.png"
	case $c in
		+([[:lower:]])) /e/Programs/ImageMagick/convert.exe -background none -fill black -font ../../font/chess.ttf -pointsize 512 label:"$c" "$c.png";;
		+([[:upper:]])) /e/Programs/ImageMagick/convert.exe -background none -fill black -font ../../font/chess.ttf -pointsize 512 label:"$c" "${c}_uppercase.png";;
		*)               /e/Programs/ImageMagick/convert.exe -background none -fill black -font ../../font/chess.ttf -pointsize 512 label:"$c" "plus.png";;
	esac
done
