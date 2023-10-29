package net.pzdcrp.shimeji.models;

import java.awt.Point;
import java.awt.Rectangle;

public class AABB {
	public float minX;
	public float minY;
	public float maxX;
	public float maxY;
	public int id = -1;
	
	public AABB(Rectangle r) {
		this.minX = r.x;
	    this.minY = r.y;
	    this.maxX = r.width;
	    this.maxY = r.height;
	}
	
	public void setid(int id) {
		this.id=id;
	}

	public AABB(float minx, float miny, float maxx, float maxy) {
	    this.minX = minx;
	    this.minY = miny;
	    this.maxX = maxx;
	    this.maxY = maxy;
	}
	
	public AABB clone() {
	    return new AABB(this.minX, this.minY, this.maxX, this.maxY);
	}
	
	public AABB grow(float x, float y) {
        return new AABB(this.getMinX() - x, this.getMinY() - y, this.getMaxX() + x, this.getMaxY() + y);
    }
	
	public AABB extend(float dx, float dy) {
	    if (dx < 0) this.minX += dx;
	    else this.maxX += dx;

	    if (dy < 0) this.minY += dy;
	    else this.maxY += dy;

	    return this;
	}
	
	public AABB offset(Point a) {
		this.minX += a.x;
	    this.minY += a.y;
	    this.maxX += a.x;
	    this.maxY += a.y;
	    return this;
	}
	
	public AABB contract(float x, float y) {
	    this.minX += x;
	    this.minY += y;
	    this.maxX -= x;
	    this.maxY -= y;
	    return this;
	}
	
	public AABB expand(float x, float y) {
	    this.minX -= x;
	    this.minY -= y;
	    this.maxX += x;
	    this.maxY += y;
	    return this;
	}
	
	public AABB offset(float x, float y) {
	    this.minX += x;
	    this.minY += y;
	    this.maxX += x;
	    this.maxY += y;
	    return this;
	}
	
	public boolean collide(AABB other) {
	    return this.minX < other.maxX && this.maxX > other.minX &&
	           this.minY < other.maxY && this.maxY > other.minY;
	}

	public float getMinX() {
		return minX;
	}

	public void setMinX(float minX) {
		this.minX = minX;
	}

	public float getMinY() {
		return minY;
	}

	public void setMinY(float minY) {
		this.minY = minY;
	}

	public float getMaxX() {
		return maxX;
	}

	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}

	public float getMaxY() {
		return maxY;
	}

	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}
	
	public AABB setBB(AABB bb) {
        this.setMinX(bb.getMinX());
        this.setMinY(bb.getMinY());
        this.setMaxX(bb.getMaxX());
        this.setMaxY(bb.getMaxY());
        return this;
    }

	@Override
	public String toString() {
		return "AABB [minX=" + minX + ", minY=" + minY + ", maxX=" + maxX + ", maxY=" + maxY + "]";
	}

	public AABB addCoord(float x, float y) {
        float minX = this.getMinX();
        float minY = this.getMinY();
        float maxX = this.getMaxX();
        float maxY = this.getMaxY();

        if (x < 0) minX += x;
        if (x > 0) maxX += x;

        if (y < 0) minY += y;
        if (y > 0) maxY += y;

        return new AABB(minX, minY, maxX, maxY);
    }

	public AABB setBounds(float minX, float minY, float maxX, float maxY) {
        this.setMinX(minX);
        this.setMinY(minY);
        this.setMaxX(maxX);
        this.setMaxY(maxY);
        return this;
    }
	
	public float calculateXOffset(AABB bb, float x) {
	    if (bb.getMaxY() <= this.getMinY() || bb.getMinY() >= this.getMaxY()) {
	        return x;
	    }
	    if (x > 0 && bb.getMaxX() <= this.getMinX()) {
	        float x1 = this.getMinX() - bb.getMaxX();
	        if (x1 < x) {
	            x = x1;
	        }
	    }
	    if (x < 0 && bb.getMinX() >= this.getMaxX()) {
	        float x2 = this.getMaxX() - bb.getMinX();
	        if (x2 > x) {
	            x = x2;
	        }
	    }
	    return x;
	}

	public float calculateYOffset(AABB bb, float y) {
	    if (bb.getMaxX() <= this.getMinX() || bb.getMinX() >= this.getMaxX()) {
	        return y;
	    }
	    if (y > 0 && bb.getMaxY() <= this.getMinY()) {
	        float y1 = this.getMinY() - bb.getMaxY();
	        if (y1 < y) {
	            y = y1;
	        }
	    }
	    if (y < 0 && bb.getMinY() >= this.getMaxY()) {
	        float y2 = this.getMaxY() - bb.getMinY();
	        if (y2 > y) {
	            y = y2;
	        }
	    }
	    return y;
	}
}
