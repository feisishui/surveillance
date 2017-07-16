package com.casic.patrol.user.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**位置信息
 * Created by yangmeng on 2017/1/16.
 */
@Entity
@Table(name = "CD_POSITION")
@SequenceGenerator(name = "CD_POSITIONIS", sequenceName = "CD_POSITIONIS",allocationSize=1,initialValue=1)
public class Position implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;
	private double longitude;
	private double latitude;
	private Date locaTime;
	private User user;


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_POSITIONIS")
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Column(name = "LONGITUDE")
	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	@Column(name = "LATITUDE")
	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	@Column(name = "LOCATIME")
	public Date getLocaTime() {
		return locaTime;
	}

	public void setLocaTime(Date locaTime) {
		this.locaTime = locaTime;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "USER_ID")

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
