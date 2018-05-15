for i in {1..7874}
do
    echo "https://pixabay.com/zh/photos/?order=popular&pagi=$i"
    curl --cookie "g_rated=1" "https://pixabay.com/zh/photos/?order=popular&pagi=$i"  > ../data/$i
done