//groovy movieRatingDataGenerator

String getRandomString() {
    UUID.randomUUID().toString()
}

Map createSampleMovieReviewData() {
    [
            id: getRandomString(),
            name: "A"
    ]
}