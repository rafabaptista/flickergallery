package com.rafabap.flickergallery.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rafabap.flickergallery.R
import com.rafabap.flickergallery.databinding.PhotoListLayoutBinding
import com.rafabap.flickergallery.domain.model.Photo
import com.squareup.picasso.Picasso

class PhotoListAdapter(private val adapterEvents: PhotoListViewHolder.PhotoListAdapterEvents)
    : ListAdapter<Photo, PhotoListAdapter.PhotoListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        return PhotoListViewHolder.create(parent, adapterEvents)
    }

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(
                oldItem: Photo,
                newItem: Photo
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Photo,
                newItem: Photo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class PhotoListViewHolder(
        private val itemBinding: PhotoListLayoutBinding,
        private val adapterEvents: PhotoListAdapterEvents
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(photo: Photo) {
            itemBinding.run {
                Picasso.get()
                    .load(photo.normalPhotoUrl)
                    .fit()
                    .centerInside()
                    .placeholder(R.mipmap.empty_photo)
                    .error(R.mipmap.empty_photo)
                    .into(imageViewPhoto)
                imageViewPhoto.setOnClickListener {
                    adapterEvents.onPhotoClick(imageViewPhoto, photo.largePhotoUrl)
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                adapterEvents: PhotoListAdapterEvents
            ): PhotoListViewHolder {
                val itemBinding = PhotoListLayoutBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return PhotoListViewHolder(
                    itemBinding,
                    adapterEvents
                )
            }
        }

        interface PhotoListAdapterEvents {
            fun onPhotoClick(imageViewPhoto: ImageView, photoUrl: String)
        }
    }
}