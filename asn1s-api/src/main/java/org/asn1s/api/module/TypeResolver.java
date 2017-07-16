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

package org.asn1s.api.module;


import org.asn1s.api.Ref;
import org.asn1s.api.exception.ResolutionException;
import org.asn1s.api.type.DefinedType;
import org.asn1s.api.type.Type;
import org.asn1s.api.type.TypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface TypeResolver
{
	/**
	 * Registers type in this module
	 *
	 * @param type named type
	 */
	void add( @NotNull DefinedType type );

	void addImports( @NotNull ModuleReference moduleReference, @NotNull Collection<String> symbols );

	/**
	 * Returns collection of types present in this module
	 *
	 * @return {@link Collection} of {@link DefinedType}
	 */
	@NotNull
	Collection<DefinedType> getTypes();

	@NotNull
	Ref<Type> getTypeRef( @NotNull String ref, @Nullable String module );

	/**
	 * Return type declared in this module
	 *
	 * @param name type name
	 * @return {@link DefinedType} or null
	 */
	@Nullable
	DefinedType getType( @NotNull String name );

	@NotNull
	Type resolve( @NotNull TypeName typeName ) throws ResolutionException;
}