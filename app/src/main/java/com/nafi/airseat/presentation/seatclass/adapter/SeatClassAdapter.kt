import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nafi.airseat.data.model.SeatClass
import com.nafi.airseat.databinding.ItemClassBinding

class SeatClassAdapter(private val itemClick: (SeatClass) -> Unit) :
    ListAdapter<SeatClass, SeatClassAdapter.ItemSeatClassViewHolder>(SeatClassDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemSeatClassViewHolder {
        val binding =
            ItemClassBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ItemSeatClassViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(
        holder: ItemSeatClassViewHolder,
        position: Int,
    ) {
        holder.bindView(getItem(position))
    }

    class ItemSeatClassViewHolder(
        private val binding: ItemClassBinding,
        private val itemClick: (SeatClass) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: SeatClass) {
            with(binding) {
                optionText.text = item.seatName
                root.setOnClickListener { itemClick(item) }
            }
        }
    }

    class SeatClassDiffCallback : DiffUtil.ItemCallback<SeatClass>() {
        override fun areItemsTheSame(
            oldItem: SeatClass,
            newItem: SeatClass,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SeatClass,
            newItem: SeatClass,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
