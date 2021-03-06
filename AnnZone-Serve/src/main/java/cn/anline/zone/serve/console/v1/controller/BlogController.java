package cn.anline.zone.serve.console.v1.controller;

import act.controller.Controller;
import static act.controller.Controller.Util.*;

import act.db.ebean2.EbeanDao;
import cn.anline.zone.serve.console.v1.config.Constant;
import cn.anline.zone.serve.console.v1.model.Ann_blog;
import cn.anline.zone.serve.console.v1.model.Ann_blog_category;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.result.RenderJSON;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * 博客接口
 */
@Controller("blog")
public class BlogController extends V1BaseController {

    @Inject
    EbeanDao<Long,Ann_blog> annBlogEbeanDao;

    @Inject
    EbeanDao<Long,Ann_blog_category> annBlogCategoryEbeanDao;


    /**
     * 博客文章发布接口
     * @param ann_blog
     * @return
     */
    @PostAction("publish")
    public RenderJSON publish(Ann_blog ann_blog){

        if (null == ann_blog || ann_blog.getContent().trim().equals("")){
            v1BaseBean.setExp(-1);
            v1BaseBean.setStatus(1);
            v1BaseBean.setResult(0);
            v1BaseBean.setTime(new Date().getTime());
            v1BaseBean.setMsg("博客正文不能为空");
            throw json(v1BaseBean);
        }

        ann_blog.setCreate_time(new Date().getTime());
        ann_blog.setUpdate_time(new Date().getTime());
        ann_blog.setUid(Long.valueOf(__uid));
        ann_blog.setHttp_info(context.req().fullUrl());
        ann_blog.setIp(context.req().ip());
        ann_blog.setUa(context.req().userAgentStr());

        try {
            Ann_blog annBlog = annBlogEbeanDao.save(ann_blog);
            v1BaseBean.setMsg("博文保存成功！");
            v1BaseBean.setStatus(0);
            v1BaseBean.setExp(Constant.exp+new Date().getTime());
            v1BaseBean.setTime(new Date().getTime());
            v1BaseBean.setData(annBlog);
            v1BaseBean.setResult(1);
            return json(v1BaseBean);
        }catch (Exception e){
            e.printStackTrace();
            v1BaseBean.setStatus(7);
            v1BaseBean.setResult(0);
            v1BaseBean.setExp(-1);
            v1BaseBean.setTime(new Date().getTime());
            v1BaseBean.setMsg("博文插入数据库异常！");
            return json(v1BaseBean);
        }


    }


    /**
     * 获取总数接口
     * @return
     */
    @GetAction("count")
    public RenderJSON count(){
        long count = annBlogEbeanDao.count();
        v1BaseBean.setData(count);
        v1BaseBean.setTime(new Date().getTime());
        v1BaseBean.setResult(1);
        v1BaseBean.setExp(new Date().getTime()+Constant.exp);
        v1BaseBean.setStatus(0);
        v1BaseBean.setMsg("博客文章总数获取成功！");
        return json(v1BaseBean);
    }

    @GetAction("list")
    public RenderJSON list(){
        List<Ann_blog> ann_blogs = annBlogEbeanDao.findAllAsList();
        v1BaseBean.setData(ann_blogs);
        v1BaseBean.setMsg("博客列表获取成功！");
        v1BaseBean.setStatus(0);
        v1BaseBean.setExp(Constant.exp+new Date().getTime());
        v1BaseBean.setTime(new Date().getTime());
        v1BaseBean.setResult(ann_blogs.size());
        return json(v1BaseBean);
    }
}
