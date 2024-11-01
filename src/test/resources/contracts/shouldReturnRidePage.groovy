package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Get all rides"
    request {
        url("/api/v1/rides")
        method GET()
    }
    response {
        status OK()
        headers {
            contentType("application/json")
        }
        body(
                items: [
                        [
                                id: 3,
                                passengerId: 1,
                                departureAddress: 'Departure address 1',
                                destinationAddress: 'Destination address 1',
                                rideStatus: 'CREATED'
                        ],
                        [
                                id: 2,
                                passengerId: 2,
                                departureAddress: 'Departure address 1',
                                destinationAddress: 'Destination address 1',
                                rideStatus: 'CREATED'
                        ],
                        [
                                id: 1,
                                passengerId: 1,
                                departureAddress: 'Departure address 1',
                                destinationAddress: 'Destination address 1',
                                rideStatus: 'CREATED'
                        ],
                ],
                page: 0,
                size: 10,
                total: 3
        )
    }
}
