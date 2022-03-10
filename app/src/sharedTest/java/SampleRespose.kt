import com.jerry.johnlewis.model.*

fun getProductListResponse() : ProductListResponse {
    return ProductListResponse(
        products = listOf(
            ProductListItem(
                productId = "1",
                title = "title1",
                image = "image1",
                price = Price(
                    currency = null,
                    was = null,
                    then1 = null,
                    then2 = null,
                    now = "1.00"
                )
            ),
            ProductListItem(
                productId = "2",
                title = "title2",
                image = "image2",
                price = Price(
                    currency = null,
                    was = null,
                    then1 = null,
                    then2 = null,
                    now = "2.00"
                )
            ),
            ProductListItem(
                productId = "3",
                title = "title3",
                image = "image3",
                price = Price(
                    currency = null,
                    was = null,
                    then1 = null,
                    then2 = null,
                    now = "3.00"
                )
            )
        )
    )
}

fun getProduct() : Product {
    return Product(
        title = "this is title",
        media = Media (
            images = ImageList(
                urls = listOf(
                    "image1", "image2","image3","image4","image5"
                )
            )
        ),
        price = Price(
            currency = null,
            was = null,
            then1 = null,
            then2 = null,
            now = "1.00"
        ),
        displaySpecialOffer = "This is displaySpecialOffer",
        additionalServices = AdditionalServices(
            includedServices = listOf(
                "includedService1", "includedService2"
            )
        ),
        details = Details(
            productInformation = "this is productInformation",
            features = listOf(
                Features (
                    attributes = listOf(
                        Attribute(
                            name ="Attribute Name 1",
                            value ="value 1",
                        ),
                        Attribute(
                            name ="Attribute Name 2",
                            value ="value 2",
                        ),
                        Attribute(
                            name ="Attribute Name 3",
                            value ="value 3",
                        ),
                        Attribute(
                            name ="Attribute Name 4",
                            value ="value 4",
                        ),
                        Attribute(
                            name ="Attribute Name 5",
                            value ="value 6",
                        ),
                        Attribute(
                            name ="Attribute Name 7",
                            value ="value 7",
                        ),
                        Attribute(
                            name ="Attribute Name 8",
                            value ="value 8",
                        ),
                    )
                )
            )
        ),
        code = "this is code"
    )
}