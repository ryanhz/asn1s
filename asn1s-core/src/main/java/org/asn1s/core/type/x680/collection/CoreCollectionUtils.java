////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010-2017. Lapinin "lastrix" Sergey.                          /
//                                                                             /
// Permission is hereby granted, free of charge, to any person                 /
// obtaining a copy of this software and associated documentation              /
// files (the "Software"), to deal in the Software without                     /
// restriction, including without limitation the rights to use,                /
// copy, modify, merge, publish, distribute, sublicense, and/or                /
// sell copies of the Software, and to permit persons to whom the              /
// Software is furnished to do so, subject to the following                    /
// conditions:                                                                 /
//                                                                             /
// The above copyright notice and this permission notice shall be              /
// included in all copies or substantial portions of the Software.             /
//                                                                             /
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,             /
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES             /
// OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND                    /
// NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT                /
// HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,                /
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING                /
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE                  /
// OR OTHER DEALINGS IN THE SOFTWARE.                                          /
////////////////////////////////////////////////////////////////////////////////

package org.asn1s.core.type.x680.collection;

import org.apache.commons.lang3.StringUtils;
import org.asn1s.api.encoding.EncodingInstructions;
import org.asn1s.api.encoding.IEncoding;
import org.asn1s.api.encoding.tag.TagClass;
import org.asn1s.api.encoding.tag.TagEncoding;
import org.asn1s.api.exception.ValidationException;
import org.asn1s.api.type.ComponentType;
import org.asn1s.api.type.ComponentType.Kind;
import org.asn1s.api.type.NamedType;
import org.asn1s.api.type.Type;
import org.asn1s.api.type.Type.Family;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

final class CoreCollectionUtils
{
	private CoreCollectionUtils()
	{
	}

	static void assertTagAmbiguityImpl( Collection<ComponentType> components ) throws ValidationException
	{
		Iterable<ComponentType> list = new LinkedList<>( components );
		Iterator<ComponentType> iterator = list.iterator();
		while( iterator.hasNext() )
		{
			ComponentType component = iterator.next();
			iterator.remove();
			assertTags( component, list );
		}
	}

	static void assertTags( NamedType component, Iterable<ComponentType> list ) throws ValidationException
	{
		if( component.getFamily() == Family.CHOICE && component.getEncoding( EncodingInstructions.TAG ) == null )
		{
			for( NamedType namedType : component.getNamedTypes() )
				assertTags( namedType, list );
		}
		else
		{
			TagEncoding encoding = (TagEncoding)component.getEncoding( EncodingInstructions.TAG );
			assertTagsImpl( component.getName(), encoding.getTagClass(), encoding.getTagNumber(), list );
		}
	}

	private static void assertTagsImpl( String name, TagClass tagClass, int tagNumber, Iterable<? extends NamedType> list ) throws ValidationException
	{
		for( NamedType component : list )
		{
			IEncoding enc = component.getEncoding( EncodingInstructions.TAG );
			if( enc == null )
			{
				if( component.getFamily() != Family.CHOICE )
					throw new IllegalStateException();

				assertTagsImpl( name, tagClass, tagNumber, component.getNamedTypes() );
			}
			else
			{
				TagEncoding encoding = (TagEncoding)enc;
				if( tagClass == encoding.getTagClass() && tagNumber == encoding.getTagNumber() )
					throw new ValidationException( "Duplicate tag detected for component '" + name + "' and '" + component.getName() + '\'' );
			}
		}
	}

	static String buildComponentString( AbstractCollectionType type )
	{
		StringBuilder sb = new StringBuilder( " { " );
		String delimiter = ", ";
		List<Type> components = type.getComponents( Kind.PRIMARY );
		if( !components.isEmpty() )
			sb.append( StringUtils.join( components, delimiter ) );

		List<Type> extensions = type.getComponents( Kind.EXTENSION );
		if( !extensions.isEmpty() )
		{
			if( !components.isEmpty() )
				sb.append( delimiter );

			sb.append( "..." ).append( StringUtils.join( extensions, delimiter ) );
		}

		List<Type> componentsLast = type.getComponents( Kind.SECONDARY );
		if( !componentsLast.isEmpty() )
			sb.append( delimiter ).append( "..." ).append( StringUtils.join( componentsLast, delimiter ) );

		if( type.isExtensible() )
			sb.append( delimiter ).append( "..." );

		sb.append( " }" );
		return sb.toString();
	}
}
