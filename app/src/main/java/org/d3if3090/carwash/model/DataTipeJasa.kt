package org.d3if3090.carwash.model

class DataTipeJasa {
    public fun getData():List<TipeJasa>{
        return listOf(
            TipeJasa("Cuci Mobil Manual", 30000, "30.000"),
            TipeJasa("Cuci Mobil Waterless", 50000, "50.000"),
            TipeJasa("Cuci Mobil Touchless", 50000, "50.000"),
            TipeJasa("Cuci Mobil Robotic", 80000, "80.000"),
            TipeJasa("Cuci Mobil Hidrolik", 30000, "30.000")
        )
    }
}